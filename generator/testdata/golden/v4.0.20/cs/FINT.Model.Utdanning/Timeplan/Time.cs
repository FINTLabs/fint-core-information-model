using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Timeplan
{
	public class Time {
		public enum Relasjonsnavn
        {
			UNDERVISNINGSGRUPPE,
			UNDERVISNINGSFORHOLD,
			ROM
        }
        
	
		public string Beskrivelse { get; set; }
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		public Periode Tidsrom { get; set; }
		
	}
}
