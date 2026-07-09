using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Datautstyr
{
	public class Enhetsgruppemedlemskap {
		public enum Relasjonsnavn
        {
			DIGITALENHET,
			ENHETSGRUPPE
        }
        
	
		public Identifikator SystemId { get; set; }
		
	}
}
