using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Regnskap
{
	public class Leverandor {
		public enum Relasjonsnavn
        {
			PERSON,
			LEVERANDORGRUPPE,
			VIRKSOMHET
        }
        
	
		public string Kontonummer { get; set; }
		public Identifikator Leverandornummer { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
