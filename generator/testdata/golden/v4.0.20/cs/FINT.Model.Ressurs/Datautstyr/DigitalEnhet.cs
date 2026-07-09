using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Datautstyr
{
	public class DigitalEnhet {
		public enum Relasjonsnavn
        {
			ADMINISTRATOR,
			EIER,
			PERSONALRESSURS,
			ELEV,
			STATUS,
			ENHETSTYPE,
			PLATTFORM,
			PRODUSENT,
			ENHETSGRUPPEMEDLEMSKAP
        }
        
	
		public Identifikator DataobjektId { get; set; }
		public bool? Flerbrukerenhet { get; set; }
		public string Navn { get; set; }
		public bool? Privateid { get; set; }
		public string Serienummer { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
