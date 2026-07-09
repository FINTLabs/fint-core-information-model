using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Fravarsregistrering {
		public enum Relasjonsnavn
        {
			REGISTRERTAV,
			FAGGRUPPE,
			UNDERVISNINGSGRUPPE,
			FRAVARSTYPE,
			ELEVFRAVAR
        }
        
	
		public bool ForesPaVitnemal { get; set; }
		public string Kommentar { get; set; }
		public Periode Periode { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
