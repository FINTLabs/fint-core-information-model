using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{
	public class Klassifikasjonssystem {
		public enum Relasjonsnavn
        {
			KLASSIFIKASJONSTYPE,
			ARKIVDEL
        }
        
	
		public string AvsluttetAv { get; set; }
		public DateTime? AvsluttetDato { get; set; }
		public string Beskrivelse { get; set; }
		public List<Klasse> Klasse { get; set; }
		public string OpprettetAv { get; set; }
		public DateTime OpprettetDato { get; set; }
		public Identifikator SystemId { get; set; }
		public string Tittel { get; set; }
		
	}
}
