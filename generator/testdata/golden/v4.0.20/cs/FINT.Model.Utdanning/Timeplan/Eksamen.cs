using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Timeplan
{
	public class Eksamen {
		public enum Relasjonsnavn
        {
			ROM,
			EKSAMENSGRUPPE
        }
        
	
		public string Beskrivelse { get; set; }
		public string Navn { get; set; }
		public DateTime? Oppmotetidspunkt { get; set; }
		public Identifikator SystemId { get; set; }
		public Periode Tidsrom { get; set; }
		
	}
}
