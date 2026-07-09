using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Faktura
{
	public class Faktura {
		public enum Relasjonsnavn
        {
			FAKTURAGRUNNLAG
        }
        
	
		public Adresse Adresse { get; set; }
		public long Belop { get; set; }
		public bool? Betalt { get; set; }
		public DateTime Dato { get; set; }
		public Identifikator Fakturanummer { get; set; }
		public bool? Fakturert { get; set; }
		public DateTime Forfallsdato { get; set; }
		public bool? Kreditert { get; set; }
		public string Mottaker { get; set; }
		public long? Restbelop { get; set; }
		
	}
}
