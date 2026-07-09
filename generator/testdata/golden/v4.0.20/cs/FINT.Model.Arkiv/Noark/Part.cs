using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{
	public class Part {
		public enum Relasjonsnavn
        {
			PARTROLLE
        }
        
	
		public Adresse Adresse { get; set; }
		public string Fodselsnummer { get; set; }
		public Kontaktinformasjon Kontaktinformasjon { get; set; }
		public string Kontaktperson { get; set; }
		public string Organisasjonsnummer { get; set; }
		public string PartNavn { get; set; }
		
	}
}
