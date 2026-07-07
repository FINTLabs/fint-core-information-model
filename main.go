package main

import (
	"os"

	"github.com/urfave/cli"
)

func main() {

	app := cli.NewApp()
	app.Name = Name
	app.Version = Version
	app.Author = "FINTLabs"
	app.Email = ""
	app.Usage = "Produces a canonical metamodel.json from the FINT EA XMI export " +
		"and generates the Kotlin model library from it. This utility is mainly " +
		"for internal FINT use, but if you find it usefull, please use it!"

	app.Flags = GlobalFlags
	app.Commands = Commands
	app.CommandNotFound = CommandNotFound

	app.Run(os.Args)
}
