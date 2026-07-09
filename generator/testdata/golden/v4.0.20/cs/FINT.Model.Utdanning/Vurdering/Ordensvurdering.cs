using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public abstract class Ordensvurdering {
		public enum Relasjonsnavn
        {
			ATFERD,
			ORDEN,
			SKOLEAR
        }
        
	
		public string Kommentar { get; set; }
		public Identifikator SystemId { get; set; }
		public DateTime Vurderingsdato { get; set; }
		
	}
}
