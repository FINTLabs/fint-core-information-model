using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Personvern.Samtykke
{
	public class Samtykke {
		public enum Relasjonsnavn
        {
			BEHANDLING,
			PERSON,
			ORGANISASJONSELEMENT
        }
        
	
		public Periode Gyldighetsperiode { get; set; }
		public DateTime Opprettet { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
