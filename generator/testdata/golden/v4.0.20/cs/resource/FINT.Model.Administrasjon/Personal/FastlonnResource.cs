using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Administrasjon.Personal
{

    public class FastlonnResource : LonnResource 
    {

    
        public long Prosent { get; set; }
        
            

        public void AddLonnsart(Link link)
        {
            AddLink("lonnsart", link);
        }

        public void AddArbeidsforhold(Link link)
        {
            AddLink("arbeidsforhold", link);
        }
    }
}
