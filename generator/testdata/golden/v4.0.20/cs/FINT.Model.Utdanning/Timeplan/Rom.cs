using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Timeplan
{
	public class Rom {
		public enum Relasjonsnavn
        {
			TIME,
			EKSAMEN
        }
        
	
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
