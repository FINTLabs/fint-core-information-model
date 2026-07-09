using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Arkiv.Noark;

namespace FINT.Model.Arkiv.Kulturminnevern
{
	public class TilskuddFredaBygningPrivatEie : Saksmappe {
	
		public string Bygningsnavn { get; set; }
		public string KulturminneId { get; set; }
		public Matrikkelnummer Matrikkelnummer { get; set; }
		public Identifikator Soknadsnummer { get; set; }
		
	}
}
