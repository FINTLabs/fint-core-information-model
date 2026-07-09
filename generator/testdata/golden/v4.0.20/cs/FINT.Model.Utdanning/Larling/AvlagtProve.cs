using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Larling
{
	public class AvlagtProve {
		public enum Relasjonsnavn
        {
			PROVESTATUS,
			LARLING,
			FULLFORTKODE,
			BREVTYPE,
			BEVISTYPE
        }
        
	
		public DateTime? Provedato { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
