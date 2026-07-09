using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Elevfravar {
		public enum Relasjonsnavn
        {
			FRAVARSREGISTRERING,
			ELEVFORHOLD
        }
        
	
		public Identifikator SystemId { get; set; }
		
	}
}
