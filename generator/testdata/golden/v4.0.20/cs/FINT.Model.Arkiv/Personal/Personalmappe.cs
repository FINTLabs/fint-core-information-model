using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Arkiv.Noark;

namespace FINT.Model.Arkiv.Personal
{
	public class Personalmappe : Saksmappe {
		public new enum Relasjonsnavn
        {
			PERSON,
			LEDER,
			ARBEIDSSTED,
			PERSONALRESSURS
        }
        
	
		public Personnavn Navn { get; set; }
		
	}
}
