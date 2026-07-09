using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Administrasjon.Organisasjon
{
	public class Arbeidslokasjon : Enhet {
		public enum Relasjonsnavn
        {
			ARBEIDSFORHOLD
        }
        
	
		public Identifikator Lokasjonskode { get; set; }
		public string Lokasjonsnavn { get; set; }
		
	}
}
