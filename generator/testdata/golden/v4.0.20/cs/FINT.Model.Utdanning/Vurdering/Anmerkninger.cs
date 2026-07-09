using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Anmerkninger {
		public enum Relasjonsnavn
        {
			SKOLEAR
        }
        
	
		public int Atferd { get; set; }
		public int Orden { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
