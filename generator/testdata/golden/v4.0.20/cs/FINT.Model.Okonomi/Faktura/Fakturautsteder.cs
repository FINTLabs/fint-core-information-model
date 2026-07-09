using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Faktura
{
	public class Fakturautsteder {
		public enum Relasjonsnavn
        {
			ORGANISASJONSELEMENT,
			FAKTURAGRUNNLAG,
			VARE
        }
        
	
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
