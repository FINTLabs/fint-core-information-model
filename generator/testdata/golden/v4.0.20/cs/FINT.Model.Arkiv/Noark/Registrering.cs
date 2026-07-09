using System;
using System.Collections.Generic;



namespace FINT.Model.Arkiv.Noark
{
	public abstract class Registrering {
		public enum Relasjonsnavn
        {
			TILGANGSGRUPPE,
			ADMINISTRATIVENHET,
			ARKIVDEL,
			SAKSBEHANDLER,
			ARKIVERTAV,
			OPPRETTETAV
        }
        
	
		public DateTime? ArkivertDato { get; set; }
		public string Beskrivelse { get; set; }
		public List<Dokumentbeskrivelse> Dokumentbeskrivelse { get; set; }
		public List<string> Forfatter { get; set; }
		public Klasse Klasse { get; set; }
		public List<Korrespondansepart> Korrespondansepart { get; set; }
		public List<Merknad> Merknad { get; set; }
		public List<string> Nokkelord { get; set; }
		public string OffentligTittel { get; set; }
		public DateTime? OpprettetDato { get; set; }
		public List<Part> Part { get; set; }
		public List<string> ReferanseArkivDel { get; set; }
		public string RegistreringsId { get; set; }
		public Skjerming Skjerming { get; set; }
		public string Tittel { get; set; }
		
	}
}
