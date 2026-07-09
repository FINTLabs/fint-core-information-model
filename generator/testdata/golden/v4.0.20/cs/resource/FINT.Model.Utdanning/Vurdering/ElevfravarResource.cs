using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{

    public class ElevfravarResource 
    {

    
        public Identifikator SystemId { get; set; }
        
        public ElevfravarResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddFravarsregistrering(Link link)
        {
            AddLink("fravarsregistrering", link);
        }

        public void AddElevforhold(Link link)
        {
            AddLink("elevforhold", link);
        }
    }
}
