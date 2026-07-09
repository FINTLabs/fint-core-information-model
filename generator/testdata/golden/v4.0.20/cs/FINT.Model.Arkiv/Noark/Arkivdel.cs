using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{
	public class Arkivdel {
		public enum Relasjonsnavn
        {
			KLASSIFIKASJONSSYSTEM
        }
        
	
		public Identifikator SystemId { get; set; }
		public string Tittel { get; set; }
		
	}
}
