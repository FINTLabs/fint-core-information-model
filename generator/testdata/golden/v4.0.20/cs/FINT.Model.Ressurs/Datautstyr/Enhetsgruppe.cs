using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Datautstyr
{
	public class Enhetsgruppe {
		public enum Relasjonsnavn
        {
			ORGANISASJONSENHET,
			ENHETSTYPE,
			PLATTFORM,
			ENHETSGRUPPEMEDLEMSKAP
        }
        
	
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
