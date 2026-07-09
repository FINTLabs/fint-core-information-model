using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Okonomi.Regnskap
{
	public class Transaksjon {
		public enum Relasjonsnavn
        {
			LEVERANDOR,
			ANSVARLIG,
			VALUTA,
			POSTERING
        }
        
	
		public long Belop { get; set; }
		public string Beskrivelse { get; set; }
		public List<Bilag> Bilag { get; set; }
		public DateTime Forfallsdato { get; set; }
		public DateTime? Oppdateringstidspunkt { get; set; }
		public Identifikator TransaksjonsId { get; set; }
		public DateTime? Transaksjonstidspunkt { get; set; }
		
	}
}
