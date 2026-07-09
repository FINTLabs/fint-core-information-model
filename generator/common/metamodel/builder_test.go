package metamodel

import (
	"strings"
	"testing"

	"github.com/FINTLabs/fint-core-information-model/generator/common/types"
)

func TestBuild_FailsOnUnresolvedParent(t *testing.T) {
	classes := []*types.Class{
		{Name: "Child", Package: "fint.test", Stereotype: StereotypeDatatype, Extends: "Ghost"},
	}
	if _, err := Build(classes, "vX", ""); err == nil || !strings.Contains(err.Error(), "unresolved parent") {
		t.Fatalf("expected unresolved parent error, got %v", err)
	}
}
