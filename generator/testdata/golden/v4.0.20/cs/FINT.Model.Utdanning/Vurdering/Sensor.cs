using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Sensor {
		public enum Relasjonsnavn
        {
			SKOLERESSURS,
			EKSAMENSGRUPPE
        }
        
	
		public bool Aktiv { get; set; }
		public int? Sensornummer { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
