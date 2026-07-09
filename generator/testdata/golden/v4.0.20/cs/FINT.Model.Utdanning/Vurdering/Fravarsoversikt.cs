using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Fravarsoversikt {
		public enum Relasjonsnavn
        {
			ELEVFORHOLD,
			FAG
        }
        
	
		public Fravarsprosent Halvar { get; set; }
		public Fravarsprosent Skolear { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
