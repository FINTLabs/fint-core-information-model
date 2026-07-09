using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Utdanning.Vurdering
{

    public class SluttfagvurderingResource : FagvurderingResource 
    {

    
            

        public void AddEksamensgruppe(Link link)
        {
            AddLink("eksamensgruppe", link);
        }

        public void AddKarakterhistorie(Link link)
        {
            AddLink("karakterhistorie", link);
        }

        public void AddElevvurdering(Link link)
        {
            AddLink("elevvurdering", link);
        }
    }
}
