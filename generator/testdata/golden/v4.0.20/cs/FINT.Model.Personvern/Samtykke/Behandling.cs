using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Personvern.Samtykke
{
	public class Behandling {
		public enum Relasjonsnavn
        {
			BEHANDLINGSGRUNNLAG,
			PERSONOPPLYSNING,
			SAMTYKKE,
			TJENESTE
        }
        
	
		public bool Aktiv { get; set; }
		public string Formal { get; set; }
		public DateTime? Slettet { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
