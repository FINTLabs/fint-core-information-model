using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Faktura
{
	public class Fakturagrunnlag {
		public enum Relasjonsnavn
        {
			FAKTURA,
			FAKTURAUTSTEDER
        }
        
	
		public long? Avgiftsbelop { get; set; }
		public List<Fakturalinje> Fakturalinjer { get; set; }
		public DateTime? Leveringsdato { get; set; }
		public Fakturamottaker Mottaker { get; set; }
		public long? Nettobelop { get; set; }
		public Identifikator Ordrenummer { get; set; }
		public long? Totalbelop { get; set; }
		
	}
}
