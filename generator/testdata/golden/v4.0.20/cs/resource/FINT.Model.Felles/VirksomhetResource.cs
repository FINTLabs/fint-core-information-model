using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Felles
{

    public class VirksomhetResource : EnhetResource 
    {

    
        public Identifikator VirksomhetsId { get; set; }
        
            

        public void AddLarling(Link link)
        {
            AddLink("larling", link);
        }
    }
}
