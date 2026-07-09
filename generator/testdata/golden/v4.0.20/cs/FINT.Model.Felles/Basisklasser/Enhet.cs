using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Felles.Basisklasser
{
	public abstract class Enhet : Aktor {
	
		public Adresse Forretningsadresse { get; set; }
		public string Organisasjonsnavn { get; set; }
		public Identifikator Organisasjonsnummer { get; set; }
		
	}
}
