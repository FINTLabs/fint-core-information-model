package branches

import (
	"fmt"

	"github.com/FINTLabs/fint-core-information-model/generator/common/github"
	"github.com/urfave/cli"
)

func CmdListBranches(c *cli.Context) {
	for _, b := range github.GetBranchList(c.GlobalString("owner"), c.GlobalString("repo")) {
		fmt.Println(b)
	}
}
