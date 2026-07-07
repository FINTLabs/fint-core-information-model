package metamodel

import (
	"encoding/json"
	"fmt"
	"os"

	"github.com/FINTLabs/fint-model/common/parser"
	"github.com/urfave/cli"
)

func CmdMetamodel(c *cli.Context) error {
	owner := c.GlobalString("owner")
	repo := c.GlobalString("repo")
	filename := c.GlobalString("filename")
	tag := c.GlobalString("tag")
	force := c.GlobalBool("force")
	output := c.String("output")

	classes := parser.GetClasses(owner, repo, tag, filename, force)

	doc := Build(classes, tag, "")

	data, err := json.MarshalIndent(doc, "", "  ")
	if err != nil {
		return fmt.Errorf("marshal metamodel: %w", err)
	}
	data = append(data, '\n')

	if err := os.WriteFile(output, data, 0644); err != nil {
		return fmt.Errorf("write %s: %w", output, err)
	}

	totalTypes := 0
	for _, comp := range doc.Components {
		totalTypes += len(comp.Types)
	}
	fmt.Printf("Wrote %s (%d components, %d types)\n", output, len(doc.Components), totalTypes)
	return nil
}
