package generate

import (
	"fmt"
	"os"
	"path/filepath"
	"sort"

	"github.com/FINTLabs/fint-core-information-model/common/config"
	"github.com/FINTLabs/fint-core-information-model/common/metamodel"
	"github.com/FINTLabs/fint-core-information-model/generate/kotlin"
	"github.com/urfave/cli"
)

func CmdGenerate(c *cli.Context) {
	fromJSON := c.String("from-json")
	if fromJSON == "" {
		fmt.Fprintln(os.Stderr, "ERROR: generate requires --from-json <path>")
		fmt.Fprintln(os.Stderr, "Produce a metamodel.json first with `fint-model -t <release> metamodel -o metamodel.json`.")
		os.Exit(2)
	}

	doc, err := metamodel.Load(fromJSON)
	if err != nil {
		fmt.Fprintf(os.Stderr, "ERROR: load %s: %v\n", fromJSON, err)
		os.Exit(1)
	}

	files, err := kotlin.Files(doc)
	if err != nil {
		fmt.Fprintf(os.Stderr, "ERROR: %v\n", err)
		os.Exit(1)
	}

	if err := os.RemoveAll(config.KOTLIN_BASE_PATH); err != nil {
		fmt.Fprintf(os.Stderr, "ERROR: clean %s: %v\n", config.KOTLIN_BASE_PATH, err)
		os.Exit(1)
	}

	paths := make([]string, 0, len(files))
	for path := range files {
		paths = append(paths, path)
	}
	sort.Strings(paths)

	for _, path := range paths {
		full := filepath.Join(config.KOTLIN_BASE_PATH, filepath.FromSlash(path))
		if err := os.MkdirAll(filepath.Dir(full), 0755); err != nil {
			fmt.Fprintf(os.Stderr, "ERROR: mkdir %s: %v\n", filepath.Dir(full), err)
			os.Exit(1)
		}
		if err := os.WriteFile(full, []byte(files[path]), 0644); err != nil {
			fmt.Fprintf(os.Stderr, "ERROR: write %s: %v\n", full, err)
			os.Exit(1)
		}
	}

	fmt.Printf("Wrote %d Kotlin files to %s/ (%s %s)\n", len(files), config.KOTLIN_BASE_PATH, doc.FintVersion, config.KOTLIN_PACKAGE_BASE)
}
