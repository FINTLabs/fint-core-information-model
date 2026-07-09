using System;
using System.Collections.Generic;



namespace FINT.Model.Arkiv.Noark
{
	public abstract class Saksmappe : Mappe {
		public new enum Relasjonsnavn
        {
			SAKSMAPPETYPE,
			SAKSSTATUS,
			TILGANGSGRUPPE,
			JOURNALENHET,
			ADMINISTRATIVENHET,
			SAKSANSVARLIG
        }
        
	
		public List<Journalpost> Journalpost { get; set; }
		public string Saksaar { get; set; }
		public DateTime? Saksdato { get; set; }
		public string Sakssekvensnummer { get; set; }
		public DateTime? UtlaantDato { get; set; }
		
	}
}
