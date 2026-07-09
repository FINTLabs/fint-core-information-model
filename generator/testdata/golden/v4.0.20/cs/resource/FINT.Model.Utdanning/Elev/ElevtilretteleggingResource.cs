using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Elev
{

    public class ElevtilretteleggingResource 
    {

    
        public Identifikator SystemId { get; set; }
        
        public ElevtilretteleggingResource()
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
     
            

        public void AddElev(Link link)
        {
            AddLink("elev", link);
        }

        public void AddFag(Link link)
        {
            AddLink("fag", link);
        }

        public void AddTilrettelegging(Link link)
        {
            AddLink("tilrettelegging", link);
        }

        public void AddEksamensform(Link link)
        {
            AddLink("eksamensform", link);
        }
    }
}
