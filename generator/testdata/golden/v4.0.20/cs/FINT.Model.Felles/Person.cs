using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Felles
{
	public class Person : Aktor {
		public enum Relasjonsnavn
        {
			STATSBORGERSKAP,
			KOMMUNE,
			KJONN,
			FORELDREANSVAR,
			MALFORM,
			PERSONALRESSURS,
			MORSMAL,
			PARORENDE,
			FORELDRE,
			LARLING,
			ELEV,
			OTUNGDOM
        }
        
	
		public string Bilde { get; set; }
		public Adresse Bostedsadresse { get; set; }
		public DateTime? Fodselsdato { get; set; }
		public Identifikator Fodselsnummer { get; set; }
		public Personnavn Navn { get; set; }
		
	}
}
