package main

import (
	"fmt"
	"os"

	"github.com/FINTLabs/fint-model/branches"
	"github.com/FINTLabs/fint-model/common/metamodel"
	"github.com/FINTLabs/fint-model/generate"
	"github.com/FINTLabs/fint-model/tags"
	"github.com/urfave/cli"
)

var GlobalFlags = []cli.Flag{
	cli.StringFlag{
		EnvVar: "GITHUB_OWNER",
		Name:   "owner",
		Value:  "FINTLabs",
		Usage:  "Git repository containing model",
	},
	cli.StringFlag{
		EnvVar: "GITHUB_PROJECT",
		Name:   "repo",
		Value:  "fint-informasjonsmodell",
		Usage:  "Git repository containing model",
	},
	cli.StringFlag{
		EnvVar: "MODEL_FILENAME",
		Name:   "filename",
		Value:  "FINT-informasjonsmodell.xml",
		Usage:  "File name containing information model",
	},
	cli.StringFlag{
		EnvVar: "",
		Name:   "tag, t",
		Value:  "latest",
		Usage:  "the tag (version) of the model to generate",
	},
	cli.BoolFlag{
		EnvVar: "",
		Name:   "force, f",
		Usage:  "force downloading XMI for GitHub.",
	},
}

var Commands = []cli.Command{
	{
		Name:   "generate",
		Usage:  "emit Kotlin model sources from metamodel.json",
		Action: generate.CmdGenerate,
		Flags: []cli.Flag{
			cli.StringFlag{
				Name:  "from-json",
				Usage: "metamodel.json to read (required)",
			},
		},
	},
	{
		Name:   "metamodel",
		Usage:  "produce canonical metamodel.json from EA XMI",
		Action: metamodel.CmdMetamodel,
		Flags: []cli.Flag{
			cli.StringFlag{
				Name:  "output, o",
				Value: "metamodel.json",
				Usage: "output file path",
			},
		},
	},
	{
		Name:   "listTags",
		Usage:  "list tags",
		Action: tags.CmdListTags,
		Flags:  []cli.Flag{},
	},
	{
		Name:   "listBranches",
		Usage:  "list branches",
		Action: branches.CmdListBranches,
		Flags:  []cli.Flag{},
	},
}

func CommandNotFound(c *cli.Context, command string) {
	fmt.Fprintf(os.Stderr, "%s: '%s' is not a %s command. See '%s --help'.", c.App.Name, command, c.App.Name, c.App.Name)
	os.Exit(2)
}
