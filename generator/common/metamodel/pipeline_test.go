package metamodel

import (
	"bytes"
	"encoding/json"
	"os"
	"testing"

	"github.com/FINTLabs/fint-core-information-model/generator/common/document"
	"github.com/FINTLabs/fint-core-information-model/generator/common/parser"
)

// The committed XMI fixture must produce the golden metamodel.json byte for
// byte (generatedAt excepted). This pins the entire XMI parsing and building
// stage hermetically: any change to the parser's XPath extraction, name
// normalisation, package walking, association handling, or the builder's
// flattening and derivation shows up as a diff against the golden document.
func TestPipeline_XMIFixtureProducesGoldenDocument(t *testing.T) {
	classes := parser.ClassesFromDocument(document.Open("../../testdata/xmi/v4.0.20.xml"))
	if len(classes) == 0 {
		t.Fatalf("no classes parsed from XMI fixture")
	}

	golden, err := os.ReadFile("../../testdata/golden/v4.0.20/metamodel.json")
	if err != nil {
		t.Fatalf("read golden: %v", err)
	}
	goldenDoc, err := Load("../../testdata/golden/v4.0.20/metamodel.json")
	if err != nil {
		t.Fatalf("load golden: %v", err)
	}

	built, err := Build(classes, goldenDoc.FintVersion, goldenDoc.SourceCommit)
	if err != nil {
		t.Fatalf("build: %v", err)
	}
	built.GeneratedAt = goldenDoc.GeneratedAt

	data, err := json.MarshalIndent(built, "", "  ")
	if err != nil {
		t.Fatalf("marshal built document: %v", err)
	}
	data = append(data, '\n')

	if !bytes.Equal(data, golden) {
		diffAt := len(golden)
		for i := 0; i < len(data) && i < len(golden); i++ {
			if data[i] != golden[i] {
				diffAt = i
				break
			}
		}
		lo, hi := diffAt-120, diffAt+120
		if lo < 0 {
			lo = 0
		}
		clamp := func(b []byte, hi int) int {
			if hi > len(b) {
				return len(b)
			}
			return hi
		}
		t.Fatalf("built document differs from golden at byte %d\n--- golden ---\n…%s…\n--- built ---\n…%s…",
			diffAt, golden[lo:clamp(golden, hi)], data[lo:clamp(data, hi)])
	}
}
