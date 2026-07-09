# fint-core-information-model

The FINT core team's edition of the information model pipeline, forked
from [FINTLabs/fint-model](https://github.com/FINTLabs/fint-model)
(where the original Java/C# generator lives on). One repo owns the
whole chain:

```
EA XMI ─► metamodel.json ─► Kotlin library
```

## Layout

```
generator/   Go tool that parses the EA export and emits the sources
library/     the Kotlin library: generated sources (committed) + tests
```

Generated sources are committed, so every model bump and generator
change lands as a reviewable diff. CI keeps the stages welded
together: the committed XMI fixture must produce the golden
`metamodel.json` byte for byte, the committed Kotlin must match what
the same-commit generator produces (drift gate), and the library must
compile and pass its tests.

## Usage

```bash
cd generator

go run . -t v4.0.20 metamodel -o metamodel.json

go run . generate --from-json testdata/golden/v4.0.20/metamodel.json \
  --out ../library/src/main/kotlin

gradle -p ../library build
```

## The library

One class per model type — no `Elev` / `ElevResource` split — with all
model metadata reachable statically, from instances, and from strings,
without reflection:

```kotlin
val meta = FintModel.byPath("utdanning/elev/elev") ?: throw NotFound()
meta.isIdField("systemid")

val elev: FintResource = deserialize(payload)
elev.visitIdentifikators { field, value -> index.put(field, value) }
elev.identifikatorverdi("systemid")

Elev.relations.first { it.name == "elevforhold" }.targetPath
```

The essentials:

- **Immutable**: attribute properties are `val`; the `links` map
  (`addLink`) is the only mutable surface. Cached entities are
  thread-safe. `equals`/`hashCode`/`copy()` deliberately ignore links.
- **Metadata companions** on every concrete type (`ref`, `attributes`;
  resources add `path`, `idFields`, `relations`), plus the generated
  `FintModel` registry (`byPath` / `byRef` / `byType`).
- **Relations carry baked data**: `targetPath` for link building,
  `multiplicity` with `required`/`many` flags, and
  `bidirectional` (`null` means unidirectional) with the inverse
  relation's multiplicity.
- **`Link` stores the parsed form** (`idField` + `idValue`, or
  `unresolved` verbatim for external hrefs); `href(baseUrl, path)`
  rebuilds the wire form.
- **Zero runtime dependencies** beyond `kotlin-stdlib` and
  `java.time`. Deserialization is constructor-based, so consumers
  need `jackson-module-kotlin` (auto-registered in Spring Boot Kotlin
  apps).
- `abstrakt` model types are interfaces; everything else is a data
  class over the pre-flattened attribute list from the JSON.

## metamodel.json

Canonical, language-neutral snapshot of the model (schema 1.1):
components → types → pre-flattened attributes and relations,
`"component:Name"` cross-references, UML multiplicities shipped as
both the source string and the derived kind (`EXACTLY_ONE`,
`ZERO_OR_ONE`, `ONE_OR_MORE`, `ZERO_OR_MORE`). Dangling references
fail the build — a model that doesn't make sense never generates.
See `generator/testdata/golden/v4.0.20/metamodel.json` for the real
thing and `generator/common/metamodel/schema.go` for the schema.

## Releases

Versions are plain semver starting at `0.x` while the API settles;
each release states which model version it was generated from.
Publishing goes to [repo.fintlabs.no](https://repo.fintlabs.no) as
`no.novari:fint-core-information-model`:

```kotlin
repositories {
    maven("https://repo.fintlabs.no/releases")
}
dependencies {
    implementation("no.novari:fint-core-information-model:<version>")
}
```

The release workflow re-verifies the drift gate and the full test
suite before publishing — a release is always exactly what's
committed.

## Author

[FINTLabs](https://fintlabs.no)
