using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Utdanning.Vurdering
{

    public class HalvarsfagvurderingResource : FagvurderingResource 
    {

    
            

        public void AddElevvurdering(Link link)
        {
            AddLink("elevvurdering", link);
        }
    }
}
