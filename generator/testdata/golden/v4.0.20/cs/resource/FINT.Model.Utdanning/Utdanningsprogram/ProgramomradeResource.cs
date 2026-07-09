using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Utdanningsprogram
{

    public class ProgramomradeResource : Gruppe 
    {

    
        public ProgramomradeResource()
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
     
            

        public void AddFag(Link link)
        {
            AddLink("fag", link);
        }

        public void AddTrinn(Link link)
        {
            AddLink("trinn", link);
        }

        public void AddGrepreferanse(Link link)
        {
            AddLink("grepreferanse", link);
        }

        public void AddUtdanningsprogram(Link link)
        {
            AddLink("utdanningsprogram", link);
        }

        public void AddVigoreferanse(Link link)
        {
            AddLink("vigoreferanse", link);
        }

        public void AddGruppemedlemskap(Link link)
        {
            AddLink("gruppemedlemskap", link);
        }
    }
}
