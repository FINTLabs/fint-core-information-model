using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Felles
{
	public class Kontaktperson {
		public enum Relasjonsnavn
        {
			KONTAKTPERSON
        }
        
	
		public Kontaktinformasjon Kontaktinformasjon { get; set; }
		public Personnavn Navn { get; set; }
		public Identifikator SystemId { get; set; }
		public string Type { get; set; }
		
	}
}
