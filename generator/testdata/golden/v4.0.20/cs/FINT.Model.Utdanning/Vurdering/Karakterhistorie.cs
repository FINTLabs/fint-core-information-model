using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Karakterhistorie {
		public enum Relasjonsnavn
        {
			OPPDATERTAV,
			OPPRINNELIGKARAKTERVERDI,
			OPPRINNELIGKARAKTERSTATUS,
			KARAKTERVERDI,
			KARAKTERSTATUS
        }
        
	
		public DateTime EndretDato { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
