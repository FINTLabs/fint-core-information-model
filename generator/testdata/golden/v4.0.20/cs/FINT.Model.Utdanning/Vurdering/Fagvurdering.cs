using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public abstract class Fagvurdering {
		public enum Relasjonsnavn
        {
			FAG,
			SKOLEAR,
			KARAKTER
        }
        
	
		public string Kommentar { get; set; }
		public Identifikator SystemId { get; set; }
		public DateTime Vurderingsdato { get; set; }
		
	}
}
