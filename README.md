# fint-model

## Description

Tool for generating the FINT Kotlin model library from the EA
information model. Two-stage pipeline:

```
EA XMI ─► metamodel.json ─► Kotlin
```

`metamodel.json` is a canonical, language-neutral snapshot of the FINT
domain model and the source of truth for language emitters, so new
target languages can be added without touching the XMI parser. (The
old Java/C# emitters live in upstream FINTLabs/fint-model and in this
repo's history.)

## Usage

### Produce `metamodel.json`

```bash
fint-model -t v4.0.20 metamodel -o metamodel.json
```

Pulls the EA XMI from GitHub (`fint-informasjonsmodell`), parses it, and
writes a canonical JSON document with components, types, attributes,
relations, and inheritance.

### Generate the Kotlin library

`generate` reads `metamodel.json` only — no XMI access:

```bash
fint-model generate --from-json metamodel.json
```

Writes the source tree under `kotlin/` (one `.kt` file per model type,
a generated `FintModel` registry, and the runtime files), rooted at
package `no.novari.fint.core.model`.

### CLI

```
COMMANDS:
   generate      emit Kotlin model sources from metamodel.json
   metamodel     produce canonical metamodel.json from EA XMI
   listTags      list FINT model release tags
   listBranches  list FINT model branches
   help, h       show command help

GENERATE FLAGS:
   --from-json PATH       metamodel.json to read (required)

GLOBAL OPTIONS (used by metamodel / list*):
   --owner value          Git repository owner   (default "FINTLabs",                 $GITHUB_OWNER)
   --repo value           Git repository name    (default "fint-informasjonsmodell",  $GITHUB_PROJECT)
   --filename value       XMI filename           (default "FINT-informasjonsmodell.xml", $MODEL_FILENAME)
   --tag, -t value        model release/tag      (default "latest")
   --force, -f            re-download XMI even if cached
```

The downloaded XMI is cached in `$HOME/.fint-model/.cache`. Subsequent
`metamodel` runs reuse the cache unless `--force` is set.

## `metamodel.json` shape

```json
{
  "schemaVersion": "1.1",
  "fintVersion": "v4.0.20",
  "generatedAt": "2026-05-09T12:00:00Z",
  "components": [
    {
      "name": "utdanning-vurdering",
      "types": [
        {
          "name": "Elevvurdering",
          "stereotype": "hovedklasse",
          "parent": null,
          "path": "utdanning/vurdering/elevvurdering",
          "idFields": ["systemId"],
          "attributes": [
            { "name": "systemId",
              "type": "felles-kompleksedatatyper:Identifikator",
              "list": false, "optional": false,
              "deprecated": false,
              "inherited": false,
              "from": "utdanning-vurdering:Elevvurdering" }
          ],
          "relations": [
            { "name": "elevforhold",
              "target": "utdanning-elev:Elevforhold",
              "multiplicity": "1",
              "multiplicityKind": "EXACTLY_ONE",
              "bidirectional": {
                "isSource": true,
                "inverseName": "elevvurdering"
              },
              "deprecated": false,
              "inherited": false,
              "from": "utdanning-vurdering:Elevvurdering" }
          ]
        }
      ]
    }
  ]
}
```

Conventions:

- **Components** are URL-style lowercase names (`utdanning-vurdering`,
  `felles-kodeverk-iso`). Consumers derive language-specific package
  forms by splitting on `-`.
- **Cross-references** between types use `"component:Name"` strings.
  Primitives stay bare and lowercase: `string`, `boolean`, `date`,
  `datetime`, `int`, `long`, `float`, `double`. The closed primitive set
  is enumerated in `common/metamodel/schema.go`.
- **Stereotypes** are the EA-canonical Norwegian values: `hovedklasse`
  (the identifiable, REST-exposed kind), `datatype`, `abstrakt`,
  `referanse`.
- **Attributes and relations are pre-flattened.** Each type's
  `attributes` and `relations` arrays contain *both* its own entries
  *and* everything inherited from the parent chain (own first, then
  parent's, then grandparent's, …). Each entry carries:
  - `inherited` (bool) — `false` for the type's own entries, `true` if
    the entry was inherited from a parent.
  - `from` (`"component:Name"`) — the type that *declares* the entry.
    Populated for both own and inherited entries (own entries point at
    the type itself), so consumers can grep "who declares X" without
    special-casing.
  Pre-flattening means consumers don't re-implement the parent walk.
  Filter `inherited: false` to recover own-only when needed.
- **`multiplicity` is shipped both ways.** `multiplicity` is the
  source-of-truth UML string (`"1"`, `"0..1"`, `"0..*"`, `"1..*"`)
  and diff-friendly. `multiplicityKind` is the derived enum-friendly
  form (`EXACTLY_ONE`, `ZERO_OR_ONE`, `ONE_OR_MORE`, `ZERO_OR_MORE`)
  so consumers don't repeat the same 4-line lookup. These are UML
  *end* multiplicities — the `lower..upper` bounds of the target end —
  not database-style relationship cardinality.
- **`bidirectional`** is a single nullable struct: `null` for
  unidirectional, `{ isSource, inverseName }` when bidirectional.
  `isSource` matters chiefly for many-to-many — for 1-1 / 1-* either
  side is structurally fine.
- **No `identifiable` flag** — derive it from `idFields`: a type is
  identifiable iff `idFields != null && idFields.length > 0`. Same
  info, expressed once.
- **`path`** (REST URL fragment) is populated only for `hovedklasse`
  types — derived as `<component-with-slashes>/<lowercase-typename>`,
  e.g. `utdanning/vurdering/elevvurdering`. `null` for everything else
  (datatypes, abstracts, references aren't REST-exposed).
- **`idFields`** is the parent-chain-flattened list of attribute names
  whose type is `Identifikator`. Populated whenever the type or any of
  its ancestors has at least one such attribute (so abstract bases
  like `Begrep` get `idFields: ["systemId"]` too); `null` otherwise.

## CI integration

Recommended setup: a GitHub Action in `fint-informasjonsmodell` that
runs this tool on every EA model change, regenerates `metamodel.json`,
and commits it back to the model repo:

```bash
docker run --rm -v $(pwd):/src ghcr.io/fintlabs/fint-model:<version> \
  metamodel -o /src/metamodel.json -t <release>
```

Then every model PR carries both the unreadable XMI diff and a clean
JSON diff in the same review. Downstream emitter repos pin a tagged
version of `fint-informasjonsmodell` and read its `metamodel.json`.

## Install

### Binaries

Precompiled images are available on
[GHCR](https://github.com/FINTLabs/fint-model/pkgs/container/fint-model).

Mount the output directory as `/src`:

Linux / macOS:
```bash
docker run -v $(pwd):/src ghcr.io/fintlabs/fint-model:latest <ARGS>
```

Windows PowerShell:
```ps1
docker run -v ${pwd}:/src ghcr.io/fintlabs/fint-model:latest <ARGS>
```

### Source

```bash
gh repo clone fintlabs/fint-model
cd fint-model
go install
```

Update dependencies:

```bash
go get .
go mod vendor
go build -a
```

## Kotlin mapping

One class per model type (no `Elev` / `ElevResource` split), all
metadata reachable without reflection:

- `abstrakt` types become **interfaces** declaring their own
  attributes. This is safe because inheritance in the model only ever
  targets `abstrakt` parents; the emitter fails loudly if that
  invariant breaks.
- Every other type becomes a **`data class`** whose constructor
  parameters are the pre-flattened attribute list from the JSON, as
  nullable `var`s defaulting to `null` (partial payloads never throw,
  and Kotlin generates a no-arg constructor). Inherited attributes get
  `override` since the parent interface declares them. Types with no
  attributes become plain classes.
- A type **is a resource** — implements `FintResource` and carries a
  `links: MutableMap<String, MutableList<Link>>` — iff it is a
  `hovedklasse` or its flattened relation list is non-empty (the old
  Java `isResource` rule).
- Every concrete type carries a **metadata companion**
  (`companion object Metadata`): `FintTypeMetadata` (`type`, `ref`,
  `attributes`) for datatypes and references, `FintResourceMetadata`
  (plus `path`, `idFields`, `relations`) for resources. Relations bake
  compile-time-known data flat: `targetPath` (the target's REST path,
  for link building) and, when bidirectional, the inverse relation's
  multiplicity. Metadata is reachable statically (`Elev.idFields`),
  from an instance (`resource.metadata`), and from strings or classes
  via the generated **`FintModel`** registry (`byPath` / `byRef` /
  `byType`).
- Resources implement **`visitIdentifikators(IdentifikatorVisitor)`**
  (allocation-free iteration over declared id fields, unset ones
  included) and **`identifikator(field)`** (case-insensitive
  `when`-chain lookup).
- **`Link` stores the parsed form** — `idField` + `idValue` — not the
  href. `Link.parse(href)` decomposes incoming hrefs (last two
  segments; anything that doesn't decompose is kept verbatim in
  `unresolved`, e.g. grep.udir.no references), and
  `link.href(baseUrl, path)` rebuilds the wire form using
  `relationPath(...)` from the owning resource's metadata.
- **`FintMultiplicity`** uses UML range names (`EXACTLY_ONE`,
  `ZERO_OR_ONE`, `ONE_OR_MORE`, `ZERO_OR_MORE`) with `lower`/`upper`
  bounds and derived `required`/`many` — the two flags consumer code
  branches on. Direction is communicated by presence:
  `relation.bidirectional` is `null` for unidirectional relations.
- Primitives map to Kotlin/`java.time` types: `string → String`,
  `boolean → Boolean`, `int → Int`, `long → Long`, `float → Float`,
  `double → Double`, `date → LocalDate`, `datetime → LocalDateTime`.
- Zero dependencies beyond `kotlin-stdlib` and `java.time`.

Deliberately not emitted yet (added incrementally as they earn their
way in): serialization annotations (`_links` mapping lives in a small
Jackson module on the consumer side for now), validation annotations,
KDoc, `@Deprecated` stamping.

## Notes

- **`dateTime` vs `date`**: EA uses both forms inconsistently for
  semantically distinct concepts (date-only vs timestamp). Both
  canonicalise to lowercase primitives in `metamodel.json` (`date`
  stays `date`, `dateTime` becomes `datetime`). The Kotlin emitter
  maps them to `LocalDate` / `LocalDateTime`.
- **No compile gate yet**: the generated tree is covered by exact-output
  and invariant tests in `generate/kotlin`, but nothing compiles it in
  CI. Wiring a `kotlinc` check is the next hardening step.

## Author

[FINTLabs](https://fintlabs.no)
