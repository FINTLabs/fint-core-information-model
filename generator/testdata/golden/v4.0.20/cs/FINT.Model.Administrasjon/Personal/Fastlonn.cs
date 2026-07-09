using System;
using System.Collections.Generic;



namespace FINT.Model.Administrasjon.Personal
{
	public class Fastlonn : Lonn {
		public new enum Relasjonsnavn
        {
			LONNSART,
			ARBEIDSFORHOLD
        }
        
	
		public long Prosent { get; set; }
		
	}
}
