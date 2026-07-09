using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Personvern.Samtykke
{
	public class Tjeneste {
		public enum Relasjonsnavn
        {
			BEHANDLING
        }
        
	
		public string Navn { get; set; }
		public DateTime? Slettet { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
