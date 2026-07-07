package metamodel

import (
	"encoding/json"
	"os"
)

func Load(path string) (*Document, error) {
	data, err := os.ReadFile(path)
	if err != nil {
		return nil, err
	}
	var doc Document
	if err := json.Unmarshal(data, &doc); err != nil {
		return nil, err
	}
	return &doc, nil
}
