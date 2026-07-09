using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Elev
{
	public class Elevtilrettelegging {
		public enum Relasjonsnavn
        {
			ELEV,
			FAG,
			TILRETTELEGGING,
			EKSAMENSFORM
        }
        
	
		public Identifikator SystemId { get; set; }
		
	}
}
