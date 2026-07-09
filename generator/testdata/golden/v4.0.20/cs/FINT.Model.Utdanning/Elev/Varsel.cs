using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Elev
{
	public class Varsel {
		public enum Relasjonsnavn
        {
			UTSTEDER,
			KARAKTERANSVARLIG,
			TYPE,
			FAGGRUPPEMEDLEMSKAP
        }
        
	
		public long Fravarsprosent { get; set; }
		public DateTime Sendt { get; set; }
		public Identifikator SystemId { get; set; }
		public string Tekst { get; set; }
		
	}
}
