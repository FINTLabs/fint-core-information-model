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

go run . -t v4.1.0-rc-2 metamodel -o metamodel.json

go run . generate --from-json testdata/golden/v4.1.0-rc-2/metamodel.json \
  --out ../library/src/main/kotlin

gradle -p ../library build
```

## The library

One class per model type — no `Elev` / `ElevResource` split — with all
model metadata reachable statically, from instances, and from strings,
without reflection:

```kotlin
val meta = FintModel.byPath("utdanning", "elev", "elev") ?: throw NotFound()
meta.isIdField("systemid")

val elev: FintResource = deserialize(payload)
elev.visitIdentifikators { field, value -> index.put(field, value) }
elev.identifikatorverdi("systemid")

mappe.visitNested { field, nested -> mapLinks(nested) }

val relation = Elev.relations.first { it.name == "person" }
relation.resolveLink("person/fodselsnummer/ABC/DEF")
// Link(idField = "fodselsnummer", idValue = "ABC/DEF")
```

The essentials:

- **Immutable**: attribute properties are `val`; the `links` map
  (`addLink`) is the only mutable surface. Cached entities are
  thread-safe. `equals`/`hashCode`/`copy()` deliberately ignore links.
- **Metadata companions** on every concrete type (`ref`, `attributes`;
  resources add `path`, `name`, `isCommon`, `idFields`, `relations`),
  plus the generated `FintModel` registry:
  `byPath(domainName, packageName, resourceName)` answers REST routing;
  relations are walked via `relation.targetMetadata`.
- **Common resources have no path.** `felles:Person` is served under
  the domain and package of whoever links to it, so it reports
  `path = null`, `isCommon = true` — never an endpoint that doesn't
  exist. `Elev.Metadata.relationPath("person")` resolves it against the
  owner: `utdanning/elev/person`. When the owner is itself common, pass
  the path it was reached through:
  `Person.Metadata.relationPath("parorende", "utdanning/elev/person")`.
  `byPath` answers for common resources under every domain and package.
- **Relations carry baked data**: `targetPath` for link building (`null`
  when the target has no path of its own — use `relationPath`),
  `multiplicity` with `required`/`many` flags, and
  `bidirectional` (`null` means unidirectional) with the inverse
  relation's multiplicity.
- **Nested resources are reachable without reflection.** A resource
  held in a field carries links of its own —
  `Personalmappe.journalpost`, `.part`, `.skjerming`. `visitNested`
  hands each one to a visitor with its field name, lists element by
  element, unset fields skipped. It goes one level, so recurse through
  it to walk a whole tree.
- **`Link` stores the parsed form** (`idField` + `idValue`, or
  `unresolved` verbatim). Read hrefs with
  `relation.resolveLink(href)`, which splits on the target's declared id
  fields rather than by position — so an id value containing `/`
  survives, and an href naming no id field, like a Grep reference, stays
  unresolved instead of having one invented. There is no href-only
  parser: nothing but the target's metadata can say where the id begins.
- **Two outbound forms, because the two readers differ.** `idHref` is
  the adapter-facing `"idfield/idvalue"`, raw — an adapter knows the id
  fields of its own resources, so it can find where the id begins just
  as `resolveLink` does, which makes that direction lossless.
  `href(baseUrl, path)` is the county-facing absolute href, with the id
  percent-encoded into a single segment because a county client has no
  model to split on. Nothing is decoded inbound in either direction.
- **Zero runtime dependencies** beyond `kotlin-stdlib` and
  `java.time`. Deserialization is constructor-based, so consumers
  need `jackson-module-kotlin` (auto-registered in Spring Boot Kotlin
  apps).
- `abstrakt` model types are interfaces; everything else is a data
  class over the pre-flattened attribute list from the JSON.

## metamodel.json

Canonical, language-neutral snapshot of the model (schema 1.2):
components → types → pre-flattened attributes and relations,
`"component:Name"` cross-references, UML multiplicities shipped as
both the source string and the derived kind (`EXACTLY_ONE`,
`ZERO_OR_ONE`, `ONE_OR_MORE`, `ZERO_OR_MORE`). Dangling references
fail the build — a model that doesn't make sense never generates.
See `generator/testdata/golden/v4.1.0-rc-2/metamodel.json` for the real
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
