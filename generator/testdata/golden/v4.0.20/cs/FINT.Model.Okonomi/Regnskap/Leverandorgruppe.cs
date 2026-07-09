using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Regnskap
{
	public class Leverandorgruppe {
		public enum Relasjonsnavn
        {
			LEVERANDOR
        }
        
	
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
