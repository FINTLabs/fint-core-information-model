using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Eiendel
{
	public class Applikasjonsressurs {
		public enum Relasjonsnavn
        {
			BRUKERTYPE,
			HANDHEVINGSTYPE,
			LISENSMODELL,
			RESSURSTILGJENGELIGHET,
			EIER,
			APPLIKASJON
        }
        
	
		public string Beskrivelse { get; set; }
		public long? Enhetskostnad { get; set; }
		public Periode Gyldighetsperiode { get; set; }
		public bool? KreverGodkjenning { get; set; }
		public long? Lisensantall { get; set; }
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
