using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Arkiv.Noark
{

    public class MerknadResource 
    {

    
        public DateTime Merknadsdato { get; set; }
        public string Merknadstekst { get; set; }
        
        public MerknadResource()
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
     
            

        public void AddMerknadstype(Link link)
        {
            AddLink("merknadstype", link);
        }

        public void AddMerknadRegistrertAv(Link link)
        {
            AddLink("merknadRegistrertAv", link);
        }
    }
}
